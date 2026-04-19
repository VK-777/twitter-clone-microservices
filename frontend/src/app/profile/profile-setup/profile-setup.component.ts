import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../core/services/user.service';
import { MediaService } from 'src/app/core/services/media.service';

@Component({
  selector: 'app-profile-setup',
  templateUrl: './profile-setup.component.html',
  styleUrls: ['./profile-setup.component.css']
})

export class ProfileSetupComponent {

  profileForm: FormGroup;
  isLoading    = false;
  bioCharsLeft = 160;
  coverPreview:  string | null = null;
  avatarPreview: string | null = null;
  coverFile:  File | null = null;
  avatarFile: File | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly mediaService: MediaService
  ) {

    this.profileForm = this.fb.group({
      bio:      [''],
      location: [''],
      website:  ['']
    });

  }

  onBioInput(): void {
    this.bioCharsLeft = 160 - (this.profileForm.get('bio')?.value?.length || 0);
  }

  onCoverSelect(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.coverFile = file;
    const reader = new FileReader();
    reader.onload = e => this.coverPreview = e.target?.result as string;
    reader.readAsDataURL(file);
  }

  onAvatarSelect(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.avatarFile = file;
    const reader = new FileReader();
    reader.onload = e => this.avatarPreview = e.target?.result as string;
    reader.readAsDataURL(file);
  }

  removeCover(event: Event): void {
    event.stopPropagation();
    this.coverPreview = null;
    this.coverFile    = null;
  }

  skip(): void {
    this.router.navigate(['/timeline']);
  }
  onSubmit(): void {
    this.isLoading = true;
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const payload = {
      bio:      this.profileForm.get('bio')?.value      || null,
      location: this.profileForm.get('location')?.value || null,
      website:  this.profileForm.get('website')?.value  || null
    };

    this.userService.updateProfile(payload).subscribe({
      next: () => {
        this.isLoading = false;
        localStorage.setItem('user', JSON.stringify({ ...user, ...payload }));
        const uploads = [];
        if (this.avatarFile) {
          uploads.push(this.mediaService.uploadProfilePhoto(this.avatarFile, user.id));
        }
        if (this.coverFile) {
          uploads.push(this.mediaService.uploadCoverPhoto(this.coverFile, user.id));
        }
        if (uploads.length > 0) {
          let done = 0;
          for(const upload of uploads){
            upload.subscribe({
              next: () => { done++; if (done === uploads.length) this.router.navigate(['/timeline']); },
              error: () => { done++; if (done === uploads.length) this.router.navigate(['/timeline']); }
            });
          }
        } else {
          this.router.navigate(['/timeline']);
        }
      },
      error: () => { this.isLoading = false; this.router.navigate(['/timeline']); }
    });
  }

}


