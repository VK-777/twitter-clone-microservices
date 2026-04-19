import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../core/services/user.service';
import { TweetService } from '../../core/services/tweet.service';
import { AuthService } from '../../core/services/auth.service';
import { MediaService } from 'src/app/core/services/media.service';

@Component({
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.css']
})

export class ViewProfileComponent implements OnInit {
  profile:     any = null;
  userTweets:  any[] = [];
  userReplies: any[] = [];
  likedTweets: any[] = [];
  suggestions: any[] = [];
  isLoading    = true;
  isOwnProfile = false;
  activeTab    = 'tweets';
  followerCount = 0;
  followingCount= 0;
  coverPhotoUrl:string | null = null;
  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly userService: UserService,
    private readonly tweetService: TweetService,
    private readonly authService: AuthService,
    private readonly mediaService: MediaService
  ) {}

  ngOnInit(): void {
    this.loadSuggestions();
    this.route.params.subscribe(params => {
      const userId = params['id'];
      const currentUser = this.authService.getCurrentUser();
      // If no id in route or id matches current user → own profile
      this.isOwnProfile = !userId || +userId === currentUser?.id;
      const targetId = userId || currentUser?.id;
      this.loadProfile(targetId);
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
          .slice(0, 3)
          .map(u => ({
            ...u,
            username: u.email?.split('@')[0],
            isFollowing: false
          }));
      },
      error: () => {
        this.suggestions = []; // empty if fails
      }
    });
  }

  loadProfile(userId: number): void {
    this.isLoading = true;
    this.userService.getUserById(userId).subscribe({

      next: (data) => {
        this.profile   = data;
        this.isLoading = false;
        this.loadUserTweets(userId);
        this.loadFollowCounts(userId);
        this.loadFollowingCounts(userId);

        this.mediaService.getCoverPhoto(userId).subscribe({
          next: (blob) => { this.coverPhotoUrl = URL.createObjectURL(blob)},
          error: () => {}
        })
      },

      error: () => {
        this.isLoading = false;
      }
    });
  }

  loadUserTweets(userId: number): void {
    this.tweetService.getUserTweets(userId).subscribe({
      next:  (data) => { this.userTweets = data; },
      error: ()     => { this.userTweets = []; }
    });
  }

  loadFollowCounts(userId: number): void{
    this.userService.getFollowersCount(userId).subscribe({
      next:(count)=>this.followerCount=count,
      error:(err)=> console.error("Followers count failed",err)
    })
  }

  loadFollowingCounts(userId: number): void{
    this.userService.getFollowingCount(userId).subscribe({
      next:(count)=>this.followingCount=count,
      error:(err)=> console.error("Following count failed",err)
    })
  }

  onFollowChanged(isNowFollowing : boolean): void{
    console.log("here")
    if(isNowFollowing){
      this.followerCount++;
    }else{
      this.followerCount--;
    }
  }

  goBack(): void { this.router.navigate(['/timeline']); }
}

