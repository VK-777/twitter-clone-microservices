import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../core/services/user.service';
import { AuthService } from '../core/services/auth.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-followers',
  templateUrl: './followers.component.html',
  styleUrls: ['./followers.component.css']
})

export class FollowersComponent implements OnInit {
  activeTab   = 'followers';
  isLoading   = false;
  currentUser: any = null;
  followers:  any[] = [];
  following:  any[] = [];

  constructor(
    private readonly userService: UserService,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadFollowers();
  }

  switchTab(tab: string): void {
    this.activeTab = tab;
    if (tab === 'followers') {
      this.loadFollowers();
    } else {
      this.loadFollowing();
    }
  }

  loadFollowers(): void {
    this.isLoading = true;
    this.userService.getFollowers(this.currentUser?.id).subscribe({
      next:  (ids:number[]) => { 
        const requests = ids.map(id => this.userService.getUserById(id));
        forkJoin(requests).subscribe({
          next:(users)=>{this.followers=users; this.isLoading=false;},
          error: () =>{ this.followers =[]; this.isLoading=false;}
        });
      },
      error: () =>{ this.following =[]; this.isLoading=false;}
    });
  }

  loadFollowing(): void {
    this.isLoading = true;
    this.userService.getFollowing(this.currentUser?.id).subscribe({
      next:  (ids:number[]) => { 
        const requests = ids.map(id => this.userService.getUserById(id));
        forkJoin(requests).subscribe({
          next:(users)=>{this.following=users; this.isLoading=false;},
          error: () =>{ this.following =[]; this.isLoading=false;}
        });
      },
      error: () =>{ this.following =[]; this.isLoading=false;}
    });
  }
  goBack(): void { this.router.navigate(['/timeline']); }
}
