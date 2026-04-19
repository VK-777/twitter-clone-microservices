import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TweetDetailComponent } from './tweet-detail.component';

describe('TweetDetailComponent', () => {
  let component: TweetDetailComponent;
  let fixture: ComponentFixture<TweetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TweetDetailComponent]
    });
    fixture = TestBed.createComponent(TweetDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
