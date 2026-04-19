import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: []
})

export class LogoutComponent {

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  confirmLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  cancel(): void {
    this.router.navigate(['/timeline']);
  }
}
