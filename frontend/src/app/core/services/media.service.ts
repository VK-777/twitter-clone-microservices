import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({ providedIn: 'root' })

export class MediaService {
  private readonly baseUrl = 'http://localhost:8090/api/v1/media';
  constructor(private readonly http: HttpClient) {}
  private getHeaders() {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return { 'X-User-Id': user.id?.toString() || '' };
  }

  uploadMedia(file: File, userId: number, tweetId: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId.toString());
    formData.append('tweetId', tweetId.toString());
    return this.http.post(`${this.baseUrl}/upload`, formData, {
      headers: this.getHeaders()
    });
  }

  uploadProfilePhoto(file: File, userId: number): Observable<any> {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('userId', userId.toString());
  formData.append('tweetId', '0');
  return this.http.post(`${this.baseUrl}/upload`, formData, {
    headers: this.getHeaders()
  });
  }

  uploadCoverPhoto(file: File, userId: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId.toString());
    formData.append('tweetId', '-1');
    return this.http.post(`${this.baseUrl}/upload`, formData, {
      headers: this.getHeaders()
    });
  }

  getProfilePhoto(userId: number): Observable<Blob> {
    return new Observable(observer => {
      this.getMediaByUser(userId).subscribe({
        next: (mediaList) => {
          const photo = mediaList.find((m: any) => m.tweetId === 0);
          if (photo) {
            this.getMediaById(photo.id).subscribe({
              next: (blob) => { observer.next(blob); observer.complete(); },
              error: (e) => observer.error(e)
            });
          } else { observer.error('none'); }
        },
        error: (e) => observer.error(e)
      });
    });
  }

  getCoverPhoto(userId: number): Observable<Blob> {
    return new Observable(observer => {
      this.getMediaByUser(userId).subscribe({
        next: (mediaList) => {
          const photo = mediaList.find((m: any) => m.tweetId === -1);
          if (photo) {
            this.getMediaById(photo.id).subscribe({
              next: (blob) => { observer.next(blob); observer.complete(); },
              error: (e) => observer.error(e)
            });
          } else { observer.error('none'); }
        },
        error: (e) => observer.error(e)
      });
    });
  }

  getMediaById(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/view/${id}`, {
      headers: this.getHeaders(),
      responseType: 'blob'
    });
  }

  getMediaByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`, {
      headers: this.getHeaders()
    });
  }

  getMediaByTweet(tweetId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/tweet/${tweetId}`, {
      headers: this.getHeaders()
    });
  }

  deleteMedia(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
}
