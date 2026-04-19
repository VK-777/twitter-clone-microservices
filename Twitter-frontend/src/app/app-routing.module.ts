import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path : '' , redirectTo: 'timeline', pathMatch:'full'},

  { path: 'login', loadChildren: () => import('./auth/login/login.module').then(m => m.LoginModule) },

  { path: 'register', loadChildren: () => import('./auth/register/register.module').then(m => m.RegisterModule) },

  { path: 'forgot-password', loadChildren: () => import('./auth/forgot-password/forgot-password.module').then(m => m.ForgotPasswordModule) },

  { path: 'profile-setup', loadChildren: () => import('./profile/profile-setup/profile-setup.module').then(m => m.ProfileSetupModule)
  },

  { path: 'profile', loadChildren: () => import('./profile/view-profile/view-profile.module').then(m => m.ViewProfileModule), 
    canActivate: [authGuard]
  },

  { path: 'profile/:id', loadChildren: () => import('./profile/view-profile/view-profile.module').then(m => m.ViewProfileModule), 
    canActivate: [authGuard]
  },

  { path: 'timeline', loadChildren: () => import('./timeline/timeline.module').then(m => m.TimelineModule)
   },

  { path: 'tweet/:id', loadChildren: () => import('./tweet-detail/tweet-detail.module').then(m => m.TweetDetailModule),
    canActivate: [authGuard]
   },

  { path: 'search', loadChildren: () => import('./search/search.module').then(m => m.SearchModule),
    canActivate: [authGuard]
   },

  { path: 'followers', loadChildren: () => import('./followers/followers.module').then(m => m.FollowersModule),
    canActivate: [authGuard]
   },

  { path: 'logout', loadChildren: () => import('./logout/logout.module').then(m => m.LogoutModule),
    canActivate: [authGuard]
   },

  { path : '**', redirectTo:'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
