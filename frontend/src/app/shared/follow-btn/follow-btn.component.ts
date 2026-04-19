import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../../core/services/user.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-follow-btn',
  template: `
    <button (click)="toggleFollow()" [disabled]="isLoading"
      class="btn btn-sm {{ isFollowing ? 'btn-outline-light' : 'btn-primary' }}">
      {{ isLoading ? '...' : (isFollowing ? 'Unfollow' : 'Follow') }}
    </button>
    `
})

export class FollowBtnComponent implements OnInit {
  @Input() userId!: number;
  @Input() initialState: boolean = false;  
  @Output() followChanged = new EventEmitter<boolean>();
  isFollowing = false;
  isLoading = false;
  constructor(
    private readonly userService: UserService,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isFollowing = this.initialState;  // ← set from input
    const currentUser = this.authService.getCurrentUser();
    if(currentUser?.id && this.userId){
      this.userService.isFollowing(currentUser.id, this.userId).subscribe({
        next: (status:boolean)=>{
          this.isFollowing =status;
        },
        error:()=>{
          this.isFollowing = this.initialState; //fallback
        }
      })
    }
  }

  toggleFollow(): void {
    const currentUser = this.authService.getCurrentUser();
    console.log("FOLLOW REQUEST:", currentUser.id, this.userId);
    if (!currentUser?.id) return;
    const previousState = this.isFollowing; 
    this.isFollowing=!this.isFollowing;
    this.followChanged.emit(this.isFollowing);
    this.isLoading = true;
    //Optimisically upodating before API call
    if (previousState) {  
      this.userService.unfollowUser(currentUser.id, this.userId).subscribe({
        next: () => { this.isLoading = false; },
        error: () => { 
          this.isFollowing = previousState; //revert if failed
          this.isLoading = false; 
        }
      });
    } else {
      this.userService.followUser(currentUser.id, this.userId).subscribe({
        next: () => { 
          this.isFollowing = previousState; 
          this.isLoading = false; 
        },
        error: () => { this.isLoading = false; }
        
      });
    }
  }
}
