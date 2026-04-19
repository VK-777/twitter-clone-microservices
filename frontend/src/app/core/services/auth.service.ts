import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from "rxjs";

@Injectable({providedIn: 'root'})
export class AuthService{
    
    private readonly baseUrl='http://localhost:8090/api/v1/users';

    constructor(private readonly http:HttpClient) {}

    login(payload: any): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/login`, payload).pipe(
            tap(res => {
            localStorage.setItem('token', res.token);
            localStorage.setItem('user', JSON.stringify({
                id:                res.user.id,
                firstName:         res.user.firstName,
                email:             res.user.email,
                username:          res.user.email.split('@')[0],
                bio:               res.user.bio,
                location:          res.user.location,
                website:           res.user.website,
                profilePictureUrl: res.user.profilePictureUrl,
                coverPictureUrl:   res.user.coverPictureUrl
                }));
            })
        );
    }

    register(payload : any) : Observable<any>{
        return this.http.post(`${this.baseUrl}/register`,payload,{responseType:'text'});
    }

    forgotPassword(email : string) : Observable<any>{
        return this.http.post<any>(`${this.baseUrl}/forgot-password`, { email });
    }

    logout() : void{
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }

    getToken() : string | null { 
        return localStorage.getItem('token')
    }

    getCurrentUser(): any {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem('token');
    }
}