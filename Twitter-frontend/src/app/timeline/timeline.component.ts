import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { TweetService } from '../core/services/tweet.service';
import { AuthService } from '../core/services/auth.service';
import { UserService } from '../core/services/user.service';
import { MediaService } from '../core/services/media.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css']
})

export class TimelineComponent implements OnInit {

  @ViewChild('composerRef') composerRef!: ElementRef<HTMLTextAreaElement>;
  tweets:      any[] = [];
  suggestions: any[] = [];
  trends:      any[] = [];
  isLoading  = false;
  isPosting  = false;
  showEmojiPicker = false;
  emojis=['😂','😍', '❤️', '👍', '👌']
  activeTab  = 'for-you';
  tweetContent = '';
  charsLeft    = 280;
  mediaPreviewUrl: string | null = null;
  selectedFile: File | null = null;
  userInitials = 'U';
  currentUser = this.authService.getCurrentUser();

  constructor(
    private readonly tweetService: TweetService,
    private readonly authService: AuthService,
    private readonly userService: UserService,
    private readonly mediaService: MediaService,
    private readonly router : Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user?.firstName) {
      this.userInitials = user.firstName.charAt(0).toUpperCase();
    }
    this.loadSuggestions();
    this.loadFeed();
    this.loadTrends();
  }

  loadTrends(): void {
    this.tweetService.getTrending().subscribe({
      next: (data)=>this.trends = data,
      error: (err) => console.error(err)
    });
  }

  loadSuggestions(): void {
    // Search for users with empty prefix to get all users
    this.userService.searchUsers("").subscribe({
      next: (users: any[]) => {
        const currentUser = this.authService.getCurrentUser();
        // Filter out current user, take first 3
        this.suggestions = users
          .filter(u => u.id !== currentUser?.id)
          .slice(0, 5)
          .map(u => ({
            ...u,
            username: u.email?.split('@')[0],
            isFollowing: u.isFollowing ?? false
          }));
      },
      error: () => {
        this.suggestions = []; // empty if fails
      }
    });
  }

  loadFeed(): void {
    this.isLoading = true;
    this.tweetService.getFeed().subscribe({
      next: (data) => { this.tweets = data;  this.isLoading = false; },
      error: ()     => { this.tweets = [];   this.isLoading = false; }
    });

  }


  onInput(): void {
    this.charsLeft = 280 - this.tweetContent.length;
  }

  onMediaSelect(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = e => this.mediaPreviewUrl = e.target?.result as string;
    reader.readAsDataURL(file);
  }

  removeMedia(): void {
    this.mediaPreviewUrl = null;
    this.selectedFile    = null;
  }
  postTweet(): void {
    if (!this.tweetContent.trim()) return;
    this.isPosting = true;
    this.tweetService.createTweet({ content: this.tweetContent }).subscribe({
      next: (newTweet) => {
        if (this.selectedFile) {
          const user = JSON.parse(localStorage.getItem('user') || '{}');
          this.mediaService.uploadMedia(this.selectedFile, user.id, newTweet.id)
            .subscribe({
              next: (media) => {
                newTweet.mediaUrl = 'http://localhost:8090' + media.fileUrl;
                this.tweets.unshift(newTweet);
              },
              error: () => {
                this.tweets.unshift(newTweet); // post tweet even if media fails
              }
            });
        } else {
          this.tweets.unshift(newTweet);
        }
        this.tweetContent = '';
        this.charsLeft = 280;
        this.mediaPreviewUrl = null;
        this.selectedFile = null;
        this.isPosting = false;
      },
      error: () => { this.isPosting = false; }
    });
  }

  addEmoji(emoji:string):void{
    this.tweetContent +=emoji;
    this.charsLeft = 280 - this.tweetContent.length;
    this.showEmojiPicker=false;
  }
  
  goToProfile(userId:number):void{
    this.router.navigate(['/profile', userId])
  }
  
  focusComposer(): void {
    this.composerRef?.nativeElement?.focus();
  } 

  onTrendClick(tag: string): void {
    this.router.navigate(['/search'], {queryParams:{q: tag, tab:'trends'}});
  }
}
