import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { UserService } from '../core/services/user.service';
import { TweetService } from '../core/services/tweet.service';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit{

  searchQuery = '';
  activeTab   = 'people';
  isLoading   = false;
  userResults:  any[] = [];
  tweetResults: any[] = [];
  trends:       any[] = [];

  private readonly searchSubject = new Subject<string>();

  constructor(
    private readonly userService: UserService,
    private readonly tweetService: TweetService,
    public readonly route : ActivatedRoute,
    public readonly router : Router
  ) {

    this.searchSubject.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(query => {
      if (query.trim()) {
        this.runSearch(query);
      } else {
        this.userResults  = [];
        this.tweetResults = [];
      }
    });
  }

  ngOnInit(): void {
    this.loadTrends();

    this.route.queryParams.subscribe(params => {
      if(params['q'] && params['tab']){
        this.searchQuery = params['q'];
        this.activeTab = params['tab'];
        this.runSearch(this.searchQuery);
      }
    })
  }

  loadTrends(): void {
    this.tweetService.getTrending().subscribe({
      next: (data)=>this.trends = data,
      error: (err) => console.error(err)
    });
  }


  onSearch(query: string): void {
    this.searchSubject.next(query);
  }

  clearSearch(): void {
    this.searchQuery  = '';
    this.userResults  = [];
    this.tweetResults = [];
  }

  setActiveTab(tab:string):void{
    this.clearSearch();
    this.activeTab=tab;
    if(this.searchQuery.trim()){
      this.runSearch(this.searchQuery);
    }
  }

  onTrendClick(tag: string): void {
    this.searchQuery = tag;
    this.activeTab = 'trends';
    this.runSearch(tag);
  }

  private runSearch(query: string): void {
    this.isLoading = true;
    if (this.activeTab === 'people') {
      this.userService.searchUsers(query).subscribe({
        next:  (data) => { this.userResults  = data; this.isLoading = false; },
        error: ()     => { this.userResults  = [];   this.isLoading = false; }
      });
    } else if (this.activeTab === 'tweets') {
      this.tweetService.searchTweets(query).subscribe({
        next:  (data) => { this.tweetResults = data; this.isLoading = false; },
        error: ()     => { this.tweetResults = [];   this.isLoading = false; }
      });
    } else if (this.activeTab === "trends") {
      this.tweetService.getTweetsByHashtag(query).subscribe({
        next:  (data) => { this.tweetResults = data; this.isLoading = false; },
        error: ()     => { this.tweetResults = [];   this.isLoading = false; } 
      });
    } else {
      this.isLoading = false;
    }
  }
}
