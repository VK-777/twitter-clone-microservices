import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root'})
export class TweetService{
    
    private readonly baseUrl="http://localhost:8090/api/v1/tweets";
    private readonly searchUrl="http://localhost:8090/api/v1/search"

    constructor(private readonly http:HttpClient){}

    getFeed(): Observable<any[]>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any[]>(`${this.baseUrl}/feed`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    getTrending(): Observable<string[]>{
        return this.http.get<string[]>(`${this.searchUrl}/trending`);
    }

    getTweetById(id: number): Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any>(`${this.baseUrl}/${id}`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    getTweetsByHashtag(tag: string): Observable<any>{
        return this.http.get<any>(`${this.baseUrl}/hashtag/${tag}`);
    }

    getUserTweets(userId: number): Observable<any[]>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    createTweet(payload: any) : Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.post<any>(this.baseUrl,payload, {
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    editTweet(id:number, payload: any) : Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.put<any>(`${this.baseUrl}/${id}`,payload, {
            headers : { 'X-User-Id' : user.id?.toString() || '', 'X-Logged-In-User': user.email||''}
        });
    }

    deleteTweet(id:number):Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.delete(`${this.baseUrl}/${id}`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''},
            responseType : 'text'
        });
    }

    likeTweet(id:number): Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.post(`${this.baseUrl}/${id}/like`,{}, {
            headers : { 'X-User-Id' : user.id?.toString() || ''},
            responseType : 'text'
        });
    }

    unlikeTweet(id:number): Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.delete(`${this.baseUrl}/${id}/like`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''},
            responseType : 'text'
        });
    }

    getReplies(tweetId:number): Observable<any[]>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any[]>(`${this.baseUrl}/${tweetId}/replies`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    postReply(tweetId:number, content:string): Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.post<any>(`${this.baseUrl}/${tweetId}/replies`,{ content },{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    searchTweets(query:string): Observable<any[]>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any[]>(`${this.searchUrl}/tweets?keyword=${query}`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    retweet(tweetId: number): Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.post<any>(`${this.baseUrl}/${tweetId}/retweet`, {}, {
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    undoRetweet(tweetId: number): Observable<any>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.delete<any>(`${this.baseUrl}/${tweetId}/retweet`, {
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    getUserReplies(userId: number): Observable<any[]>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any[]>(`${this.baseUrl}/user/${userId}/replies`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }

    getUserLikedTweets(userId: number): Observable<any[]>{
        const user= JSON.parse(localStorage.getItem('user') || '{}');
        return this.http.get<any[]>(`${this.baseUrl}/user/${userId}/likes`,{
            headers : { 'X-User-Id' : user.id?.toString() || ''}
        });
    }
}