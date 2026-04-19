import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root'})
export class UserService{
    
    private readonly baseUrl="http://localhost:8090/api/v1/users";

    private readonly followUrl="http://localhost:8090/api/v1/follow"

    constructor(private readonly http:HttpClient){}

    getUserById(id:number): Observable<any>{
        return this.http.get<any>(`${this.baseUrl}/${id}/profile`);
    }

    updateProfile(payload: any): Observable<any> {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.put(`${this.baseUrl}/profile`, payload, {
            headers: { 'X-Logged-In-User': user.email },
            responseType: 'text'
        });
    }

    searchUsers(query: string): Observable<any[]>{
        return this.http.get<any[]>(`${this.baseUrl}/search?prefix=${query}`)
    }

    getFollowers(userId:number): Observable<any[]>{
        return this.http.get<any[]>(`${this.followUrl}/followers/${userId}`);
    }

    getFollowing(userId:number): Observable<any[]>{
        return this.http.get<any[]>(`${this.followUrl}/following/${userId}`);
    }

    followUser(followerId:number, followingId:number): Observable<any>{
        return this.http.post<any>(`${this.followUrl}`, { followerId, followingId});
    }

    isFollowing(follwerId:number, followingId:number): Observable<boolean>{
        return this.http.get<boolean>(`${this.followUrl}/is-following?followerId=${follwerId}&followingId=${followingId}`);
    }
    
    unfollowUser(followerId:number, followingId:number): Observable<any>{
        return this.http.delete(`${this.followUrl}`, {
            body: {followerId, followingId},
            responseType: 'text'
        });
    }
    
    getFollowersCount(userId:number): Observable<number>{
        return this.http.get<number>(`${this.followUrl}/followers/count/${userId}`)
    }
    
    getFollowingCount(userId:number): Observable<number>{
        return this.http.get<number>(`${this.followUrl}/following/count/${userId}`)
    }

    uploadProfilePicture(file: File) : Observable<any>{
        const form = new FormData();
        form.append('file', file);
        return this.http.post<any>(`${this.baseUrl}/profile/picture`, form);
    }

    uploadCoverPhoto(file: File) : Observable<any>{
        const form = new FormData();
        form.append('file', file);
        return this.http.post<any>(`${this.baseUrl}/profile/cover`, form);
    }

}