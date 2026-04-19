package com.group1.tweetservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group1.tweetservice.client.UserClient;
import com.group1.tweetservice.dto.CreateTweetRequest;
import com.group1.tweetservice.dto.TweetAuthorDto;
import com.group1.tweetservice.dto.TweetResponseDTO;
import com.group1.tweetservice.dto.UserProfileDto;
import com.group1.tweetservice.entity.Like;
import com.group1.tweetservice.entity.Tweet;
import com.group1.tweetservice.entity.TweetComment;
import com.group1.tweetservice.entity.TweetHashtags;
import com.group1.tweetservice.entity.TweetMedia;
import com.group1.tweetservice.entity.TweetType;
import com.group1.tweetservice.exception.TweetNotFoundException;
import com.group1.tweetservice.exception.UnauthorizedException;
import com.group1.tweetservice.repository.LikeRepository;
import com.group1.tweetservice.repository.TweetCommentRepository;
import com.group1.tweetservice.repository.TweetHashtagsRepository;
import com.group1.tweetservice.repository.TweetMediaRepository;
import com.group1.tweetservice.repository.TweetRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final LikeRepository likeRepository;
    private final UserClient userServiceClient;
    private final TweetCommentRepository tweetCommentRepository;
    private final TweetHashtagsRepository tweetHashtagsRepository;
    private final TweetMediaRepository tweetMediaRepository;

    @Override
    @Transactional
    public TweetResponseDTO createTweet(CreateTweetRequest request, Long currentUserId) {
    	UserProfileDto liveUser=userServiceClient.getUserProfile(currentUserId);
        Tweet tweet = new Tweet();
        tweet.setUserId(currentUserId);
        tweet.setUserEmail((liveUser!=null&&liveUser.getEmail()!=null)?liveUser.getEmail():"unknown@user.com");
        tweet.setUserFirstName((liveUser!=null&&liveUser.getFirstName()!=null)?liveUser.getFirstName():"Twitter User");
        tweet.setContent(request.getContent());
        tweet.setTweetType(TweetType.TWEET); 
        tweet.setParentTweetId(null);
        if(request.getScheduledAt()!=null) {
        	tweet.setScheduledAt(request.getScheduledAt());
        	tweet.setIsScheduled(true);
        }
        else {
        	tweet.setIsScheduled(false);
        }
        Tweet savedTweet = tweetRepository.save(tweet);

        // --- NEW FUNCTIONALITY: Automated Hashtag Extraction ---
        if (request.getContent() != null) {
            // Regex to find all words starting with '#'
            Matcher matcher = Pattern.compile("#(\\w+)").matcher(request.getContent());
            while (matcher.find()) {
                TweetHashtags hashtag = new TweetHashtags();
                hashtag.setTweetId(savedTweet.getId());
                hashtag.setTag(matcher.group(1).toLowerCase()); // Save tag without the '#'
                tweetHashtagsRepository.save(hashtag);
            }
        }

        // --- NEW FUNCTIONALITY: Media Linking ---
        // If the frontend passed an array of Media IDs from the Media MS
        if (request.getMediaIds() != null && !request.getMediaIds().isEmpty()) {
            for (Long mediaId : request.getMediaIds()) {
                TweetMedia tweetMedia = new TweetMedia();
                tweetMedia.setTweetId(savedTweet.getId());
                tweetMedia.setMediaId(mediaId);
                tweetMediaRepository.save(tweetMedia);
            }
        }

        return mapToDto(savedTweet, currentUserId);
    }


    @Override
    public TweetResponseDTO getTweetById(Long id, Long currentUserId) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
        tweet.setViewsCount(tweet.getViewsCount() + 1);
        tweetRepository.save(tweet);
        return mapToDto(tweet, currentUserId);
    }
    @Override
    public List<TweetResponseDTO> getTweetByContent(String keyword) {
        List<Tweet> tweets = tweetRepository.findByContentContainingIgnoreCaseAndIsDeletedFalseOrderByCreatedAtDesc(keyword);
        
        return tweets.stream().map(tweet->mapToDto(tweet, null)).toList();
    }
    @Override
    public List<TweetResponseDTO> getTweetByHashtag(String hashtag) {
    	String cleanTag=hashtag.replace("#", "").toLowerCase();
    	List<TweetHashtags> tags=tweetHashtagsRepository.findByTagIgnoreCase(cleanTag);
    	List<Long> tweetIds=tags.stream().map(TweetHashtags::getTweetId).toList();
    	return tweetRepository.findAllById(tweetIds).stream().map(t->mapToDto(t, null)).toList();
    }
    @Override
    public List<String> getTop10TrendingHashtags(){
    	return tweetHashtagsRepository.findTrendingHashtags(PageRequest.of(0, 10));
    }
    @Override
    public List<TweetResponseDTO> getFeed(Long currentUserId) {
    	List<Tweet> tweets;
        return tweetRepository.findByIsScheduledFalseAndParentTweetIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc()
                .stream().map(t -> mapToDto(t, currentUserId)).collect(Collectors.toList());
    }

    @Override
    public List<TweetResponseDTO> getUserTweets(Long userId, Long currentUserId) {
        List<Tweet> tweets=  tweetRepository.findByUserIdAndParentTweetIdIsNullOrderByCreatedAtDesc(userId);
        List<Tweet> retweets = tweetRepository.findByUserIdAndTweetTypeOrderByCreatedAtDesc(userId, TweetType.RETWEET);
        
        List<TweetResponseDTO> retweetsDTO = retweets.stream()
        		.map(retweet->{
        			Tweet parent = tweetRepository.findById(retweet.getParentTweetId()).orElse(null);
        			if(parent == null) return null;
        			TweetResponseDTO dto= mapToDto(parent,currentUserId);
        			dto.setRetweeted(true);
        			return dto;
        		})
        		.filter(dto -> dto!=null)
        		.collect(Collectors.toList());
        
        List<TweetResponseDTO> result = new ArrayList<>();
        result.addAll(tweets.stream().map(t->mapToDto(t,currentUserId)).collect(Collectors.toList()));
        result.addAll(retweetsDTO);
        
        result.sort((a,b)->b.getCreatedAt().compareTo(a.getCreatedAt()));
        return result;
    }

    @Override
    public List<TweetResponseDTO> getReplies(Long tweetId, Long currentUserId) {
        return tweetRepository.findByParentTweetIdOrderByCreatedAtAsc(tweetId)
                .stream().map(t -> mapToDto(t, currentUserId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TweetResponseDTO postReply(Long tweetId, CreateTweetRequest request, Long currentUserId) {
        Tweet parent = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        UserProfileDto liveUser=userServiceClient.getUserProfile(currentUserId);
        
        Tweet reply = new Tweet();
        reply.setUserId(currentUserId);
        reply.setUserEmail((liveUser!=null&&liveUser.getEmail()!=null)?liveUser.getEmail():"unknown@user.com");
        reply.setUserFirstName((liveUser!=null&&liveUser.getFirstName()!=null)?liveUser.getFirstName():"Twitter User");
        reply.setContent(request.getContent());
        reply.setParentTweetId(tweetId);
        reply.setTweetType(TweetType.REPLY);
        parent.setRepliesCount(parent.getRepliesCount() + 1);
        tweetRepository.save(parent);

        return mapToDto(tweetRepository.save(reply), currentUserId);
    }
    @Override
    @Transactional
    public TweetResponseDTO editTweet(Long tweetId, CreateTweetRequest request, Long currentUserId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException(tweetId));

        if (!tweet.getUserId().equals(currentUserId)) {
            throw new UnauthorizedException("You can only edit your own tweets.");
        }

        tweet.setContent(request.getContent());
        if(request.getUserEmail()!=null&&!request.getUserEmail().isEmpty()) {
        	tweet.setUserEmail(request.getUserEmail());
        }
        tweet.setCreatedAt(LocalDateTime.now());
        Tweet updatedTweet = tweetRepository.save(tweet);

        // Recalculate hashtags
        tweetHashtagsRepository.deleteByTweetId(tweetId);
        if (request.getContent() != null) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("#(\\w+)").matcher(request.getContent());
            while (matcher.find()) {
                TweetHashtags hashtag = new TweetHashtags();
                hashtag.setTweetId(tweetId);
                hashtag.setTag(matcher.group(1).toLowerCase());
                tweetHashtagsRepository.save(hashtag);
            }
        }

        return mapToDto(updatedTweet, currentUserId);
    }

    @Override
    public void deleteTweet(Long tweetId, Long currentUserId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
        if (!tweet.getUserId().equals(currentUserId))
            throw new UnauthorizedException("Unauthorized to delete this tweet");
        
        //Decrease Counter Reply
        if(tweet.getTweetType()==TweetType.REPLY && tweet.getParentTweetId()!=null) {
        	tweetRepository.findById(tweet.getParentTweetId()).ifPresent(parent->{
        		parent.setRepliesCount(Math.max(0, parent.getRepliesCount()-1));
        		tweetRepository.save(parent);
        	});
        }
        
      //Decrease Counter Retweet
        if(tweet.getTweetType()==TweetType.RETWEET && tweet.getParentTweetId()!=null) {
        	tweetRepository.findById(tweet.getParentTweetId()).ifPresent(parent->{
        		parent.setRetweetsCount(Math.max(0, parent.getRetweetsCount()-1));
        		tweetRepository.save(parent);
        	});
        }

        
        tweet.setIsDeleted(true);
        tweetRepository.delete(tweet);
    }

    @Override
    @Transactional
    public void likeTweet(Long tweetId, Long currentUserId) {
        if (!likeRepository.existsByUserIdAndTweetId(currentUserId, tweetId)) {
            Like like = new Like();
            like.setUserId(currentUserId);
            like.setTweetId(tweetId);
            likeRepository.save(like);
            tweetRepository.findById(tweetId).ifPresent(t -> {
                t.setLikesCount(t.getLikesCount() + 1);
                tweetRepository.save(t);
            });
        }
    }
    @Override
    @Transactional
    public TweetResponseDTO retweet(Long originalTweetId, Long currentUserId) {
        Tweet originalTweet = tweetRepository.findById(originalTweetId)
                .orElseThrow(() -> new TweetNotFoundException(originalTweetId));
        UserProfileDto liveUser=userServiceClient.getUserProfile(currentUserId);
        
        Tweet retweet = new Tweet();
        
        retweet.setUserId(currentUserId);
        retweet.setUserEmail((liveUser!=null&&liveUser.getEmail()!=null)?liveUser.getEmail():"unknown@user.com");
        retweet.setUserFirstName((liveUser!=null&&liveUser.getFirstName()!=null)?liveUser.getFirstName():"Twitter User");
        
        retweet.setParentTweetId(originalTweetId);
        retweet.setTweetType(TweetType.RETWEET); 
        
        // Optionally copy content or leave null since it's just a reference
        retweet.setContent(""); 

        originalTweet.setRetweetsCount(originalTweet.getRetweetsCount() + 1);
        tweetRepository.save(originalTweet);

        return mapToDto(tweetRepository.save(retweet), currentUserId);
    }
    @Override
    @Transactional
    public void undoRetweet(Long originalTweetId, Long currentUserId) {
    	Tweet retweet = tweetRepository.findByParentTweetIdAndUserIdAndTweetType(originalTweetId, currentUserId, TweetType.RETWEET)
    			.orElseThrow(()-> new RuntimeException("Retweet Not Found"));
    	tweetRepository.delete(retweet);
    	
    	Tweet originalTweet = tweetRepository.findById(originalTweetId)
    			.orElseThrow(()-> new TweetNotFoundException(originalTweetId));
    	originalTweet.setRetweetsCount(Math.max(0,  originalTweet.getRetweetsCount()-1));
    	tweetRepository.save(originalTweet);
    }
    
    
    @Override
    @Transactional
    public void unlikeTweet(Long tweetId, Long currentUserId) {
        likeRepository.findByUserIdAndTweetId(currentUserId, tweetId).ifPresent(like -> {
            likeRepository.delete(like);
            tweetRepository.findById(tweetId).ifPresent(t -> {
                t.setLikesCount(Math.max(0, t.getLikesCount() - 1));
                tweetRepository.save(t);
            });
        });
    }
    @Override
    public TweetComment addComment(Long tweetId, String text, Long currentUserId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException(tweetId));

        TweetComment comment = new TweetComment();
        comment.setTweetId(tweetId);
        comment.setUserId(currentUserId);
        comment.setCommentText(text);

        tweet.setRepliesCount(tweet.getRepliesCount() + 1);
        tweetRepository.save(tweet);

        return tweetCommentRepository.save(comment);
    }
    
    
    private TweetResponseDTO mapToDto(Tweet tweet, Long currentUserId) {
        // Try to get live profile picture from UserMs
    	UserProfileDto liveUser = userServiceClient.getUserProfile(tweet.getUserId());

    	String profilePic=liveUser!=null?liveUser.getProfilePictureUrl():null;
    	String firstName=(liveUser!=null&&liveUser.getFirstName()!=null)?liveUser.getFirstName():null;
    	String username=tweet.getUserEmail()!=null?tweet.getUserEmail().split("@")[0]:"user";
        TweetAuthorDto author = new TweetAuthorDto(
                tweet.getUserId(),
                firstName,
                username,
                profilePic  // ← now populated from UserMs
        );

        boolean isLiked = currentUserId != null &&
                likeRepository.existsByUserIdAndTweetId(currentUserId, tweet.getId());
        
        boolean isRetweeted = currentUserId != null &&
                tweetRepository.existsByParentTweetIdAndUserIdAndTweetType(tweet.getId(), currentUserId, TweetType.RETWEET);
        
        List<Long> mediaIds=tweetMediaRepository.findByTweetId(tweet.getId()).stream().map(TweetMedia::getMediaId).toList();
        
        List<String> hashtags=tweetHashtagsRepository.findByTweetId(tweet.getId()).stream().map(TweetHashtags::getTag).toList();
        List<TweetComment> comments=tweetCommentRepository.findByTweetIdOrderByCreatedAtDesc(tweet.getId());
        
        return new TweetResponseDTO(
                tweet.getId(), tweet.getContent(), author,
                tweet.getLikesCount(), tweet.getRetweetsCount(), tweet.getRepliesCount(),
                tweet.getViewsCount(), isLiked, isRetweeted, tweet.getCreatedAt(), tweet.getParentTweetId(),mediaIds,hashtags,comments
        );
    }
}
