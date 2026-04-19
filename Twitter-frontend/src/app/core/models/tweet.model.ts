export interface Tweet {
  id: number;
  content: string;
  mediaUrl?: string;
  author: TweetAuthor;
  likesCount: number;
  retweetsCount: number;
  repliesCount: number;
  viewsCount: number;
  isLiked: boolean;
  isRetweeted: boolean;
  createdAt: string;
}

export interface TweetAuthor {
  id: number;
  firstName: string;
  username: string;
  profilePicture?: string;
  isFollowing?: boolean;
}

export interface CreateTweetRequest {
  content: string;
  mediaUrl?: string;
}

export interface TweetReply {
  id: number;
  content: string;
  author: TweetAuthor;
  likesCount: number;
  retweetsCount: number;
  repliesCount: number;
  viewsCount: number;
  isLiked: boolean;
  isRetweeted: boolean;
  createdAt: string;
}
