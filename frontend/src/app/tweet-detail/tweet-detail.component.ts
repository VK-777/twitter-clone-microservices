import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TweetService } from '../core/services/tweet.service';
import { AuthService } from '../core/services/auth.service';
import { MediaService } from '../core/services/media.service';

@Component({
  selector: 'app-tweet-detail',
  templateUrl: './tweet-detail.component.html',
  styleUrls: ['./tweet-detail.component.css']
})

export class TweetDetailComponent implements OnInit {

  tweet:    any    = null;
  replies:  any[]  = [];
  isLoading  = true;
  isReplying = false;
  isEditing  = false; 
  isOwnTweet = false;
  replyContent = '';
  editContent  = '';
  userInitials = 'U';
  formattedContent = '';
  currentUser = this.authService.getCurrentUser();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly tweetService: TweetService,
    private readonly authService: AuthService,
    private readonly mediaService : MediaService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user?.firstName) {
      this.userInitials = user.firstName.charAt(0).toUpperCase();
    }

    this.route.params.subscribe(params => {
      const tweetId = params['id'];
      this.loadTweet(tweetId);
    });

  }

  loadTweet(id: number): void {
    this.isLoading = true;
    this.tweetService.getTweetById(id).subscribe({
      next: (data) => {
        this.tweet    = data;
        this.isLoading = false;
        this.isOwnTweet = data.author.id === this.authService.getCurrentUser()?.id;
        this.formattedContent = data.content
          .replaceAll(/(#\w+)/g, '<span style="color:#1d9bf0">$1</span>')
          .replaceAll(/(@\w+)/g, '<span style="color:#1d9bf0">$1</span>');
        this.loadReplies(id);

        this.mediaService.getMediaByTweet(this.tweet.id).subscribe({
          next: (mediaList) => {
            if (mediaList && mediaList.length > 0) {
              const mediaId = mediaList[0].id;
              this.mediaService.getMediaById(mediaId).subscribe({
                next: (blob) => {
                  this.tweet.mediaUrl = URL.createObjectURL(blob);
                },
                error: () => {}
              });
            }
          },error: () => {}
        });
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadReplies(tweetId: number): void {
    this.tweetService.getReplies(tweetId).subscribe({
      next:  (data) => { this.replies = data.filter(t=> t.content && t.content.trim()!=''); },
      error: ()     => { this.replies = []; }
    });
  }

  onLike(): void {
    if (this.tweet.liked) {
      this.tweet.liked = false;
      this.tweet.likesCount--;
      this.tweetService.unlikeTweet(this.tweet.id).subscribe();
    } else {
      this.tweet.liked = true;
      this.tweet.likesCount++;
      this.tweetService.likeTweet(this.tweet.id).subscribe();
    }
  }

  onRetweet(event: Event): void {
    event.stopPropagation();
    if(this.tweet.retweeted) {
      this.tweet.retweeted = false;
          this.tweet.retweetsCount--;
      this.tweetService.undoRetweet(this.tweet.id).subscribe();
    }else{   
      this.tweet.retweeted = true;
      this.tweet.retweetsCount++;
      this.tweetService.retweet(this.tweet.id).subscribe();
    }
  }

  onDelete(): void {
    if (!confirm('Delete this tweet?')) return;
    this.tweetService.deleteTweet(this.tweet.id).subscribe({
      next:  () => {
        this.router.navigate(['/timeline'])
      },
      error: (err) => {
        console.error(err)
      }
    });
  }

  postReply(): void {
    if (!this.replyContent.trim()) return;
    this.isReplying = true;
    this.tweetService.postReply(this.tweet.id,this.replyContent).subscribe({
      next: (reply) => {
        this.replies.unshift(reply);
        this.replyContent = '';
        this.tweet.repliesCount++;
        this.isReplying = false;
      },
      error: () => { this.isReplying = false; }
    });
  }

  startEdit() : void{
    this.isEditing = true;
    this.editContent = this.tweet.content;
  }
  saveEdit() : void{
    if(!this.editContent.trim()) return;
    this.tweetService.editTweet( this.tweet.id,{ content: this.editContent}).subscribe({
      next: (updated)=>{
        this.tweet.content = updated.content;
        this.formattedContent = updated.content
          .replaceAll(/(#\w+)/g, '<span style="color:#1d9bf0">$1</span>')
          .replaceAll(/(@\w+)/g, '<span style="color:#1d9bf0">$1</span>');
        this.isEditing=false;
      },
      error: () =>{
        this.isEditing=false;
      }
    })
  }
  cancelEdit() : void{
    this.isEditing = false;
    this.editContent= '';
  }


  goBack(): void { history.back() }
}
