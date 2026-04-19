import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})

export class SidebarComponent implements OnInit {
  @Output() tweetClicked = new EventEmitter<void>();
  currentUser: any = null;
  userInitials = 'U';
  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser?.firstName) {
      this.userInitials = this.currentUser.firstName.charAt(0).toUpperCase();
    }
  }

  onTweet(): void {
    if(this.router.url==='/timeline'){
      this.tweetClicked.emit();
    }else{
      this.router.navigate(['/timeline']);
    }
  }

  goToLogin(): void{
    this.router.navigate(['/login']);
  }

  onLogout(): void {
    this.router.navigate(['/logout']);
  }
}
