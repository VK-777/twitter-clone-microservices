import { Component, Input, OnInit } from '@angular/core';
import { MediaService } from 'src/app/core/services/media.service';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrls: ['./avatar.component.css']
})

export class AvatarComponent implements OnInit {
  @Input() initials = 'U';
  @Input() src: string | null = null;
  @Input() size: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'xxl' = 'md';
  @Input() userId: number | null = null;

  constructor( private readonly mediaService: MediaService) {}

  ngOnInit(): void {
    if(this.userId && !this.src){
      this.mediaService.getProfilePhoto(this.userId).subscribe({
        next: (blob) => { this.src=URL.createObjectURL(blob);},
        error: () => {}
      })
    }
  }

  get bgGradient(): string {
    const gradients = [
      'linear-gradient(135deg,#1d9bf0,#0e71b8)',
      'linear-gradient(135deg,#f91880,#9b1fa1)',
      'linear-gradient(135deg,#00ba7c,#056848)',
      'linear-gradient(135deg,#ffd700,#ff8c00)',
      'linear-gradient(135deg,#a855f7,#7c3aed)',
      'linear-gradient(135deg,#ef4444,#b91c1c)',
    ];

    const idx = (this.initials.codePointAt(0) || 0) % gradients.length;
    return gradients[idx];
  }
}
