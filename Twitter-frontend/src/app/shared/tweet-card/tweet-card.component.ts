import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TweetService } from '../../core/services/tweet.service';
import { AuthService } from '../../core/services/auth.service';
import { MediaService } from 'src/app/core/services/media.service';

@Component({
  selector: 'app-tweet-card',
  templateUrl: './tweet-card.component.html',
  styleUrls: ['./tweet-card.component.css']
})

export class TweetCardComponent implements OnInit {
  @Input() tweet: any;
  @Output() tweetDeleted = new EventEmitter<number>();

  currentUser = this.authService.getCurrentUser()

  formattedContent = '';
  constructor(
    private readonly router: Router,
    private readonly tweetService: TweetService,
    private readonly authService: AuthService,
    private readonly mediaService: MediaService 
  ) {}

  ngOnInit(): void {
    this.formattedContent = this.tweet.content
      .replaceAll(/(#\w+)/g, '<span style="color:#1d9bf0">$1</span>')
      .replaceAll(/(@\w+)/g, '<span style="color:#1d9bf0">$1</span>');

    if (this.tweet?.id) {
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
      },
        error: () => {}
      });
    }
  }

  onCardClick(): void {
    if(!this.currentUser){this.router.navigate(['/login']);}
    this.router.navigate(['/tweet', this.tweet.id]);
  }

  onAuthorClick(event: Event): void {
    event.stopPropagation();
    if(!this.currentUser){this.router.navigate(['/login']);}
    this.router.navigate(['/profile', this.tweet.author.id]);
  }

  onLike(event: Event): void {
    event.stopPropagation();
    if(!this.currentUser){this.router.navigate(['/login']);}
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
    if(!this.currentUser){this.router.navigate(['/login']);}
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

}
