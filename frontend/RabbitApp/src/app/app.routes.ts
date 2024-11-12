import { RouterModule, Routes } from '@angular/router';
import { ViewProfileComponent } from './pages/view-profile/view-profile.component';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { ViewPostsRegisteredComponent } from './pages/view-posts-registered/view-posts-registered.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { TrendsComponent } from './pages/trends/trends.component';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { MapPostsComponent } from './pages/map-posts/map-posts.component';
import { ChatComponent } from './pages/chat/chat.component';

export const routes: Routes = [
    { path: 'view-profile', component: ViewProfileComponent },
    { path: 'view-posts', component: ViewPostsRegisteredComponent },
    { path: 'create-post', component: CreatePostComponent},
    { path: 'my-profile', component: UserProfileComponent},
    { path: 'trends', component: TrendsComponent},
    { path: 'analytics', component: AnalyticsComponent},
    { path: 'posts-map', component: MapPostsComponent},
    { path: 'chat', component: ChatComponent},
    { path: '', component: HomePageComponent}
];
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }