import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TweetDetailComponent } from './tweet-detail.component';

const routes: Routes = [{ path: '', component: TweetDetailComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TweetDetailRoutingModule { }
