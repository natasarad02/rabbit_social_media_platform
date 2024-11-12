import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routes';
import { ViewProfileComponent } from './pages/view-profile/view-profile.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ViewPostsRegisteredComponent } from './pages/view-posts-registered/view-posts-registered.component';
import { MatCardModule } from '@angular/material/card';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { MapComponent } from './pages/map/map.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { UpdatePostComponent } from './pages/update-post/update-post.component';
import { MapPostsComponent } from './pages/map-posts/map-posts.component';
import { TrendsComponent } from './pages/trends/trends.component';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { ChatComponent } from './pages/chat/chat.component';

import { LoginComponent } from './pages/login/login.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TokenInterceptor } from './interceptor/TokenInterceptor';



@NgModule({
  declarations: [
    AppComponent,
    ViewProfileComponent,
    ViewPostsRegisteredComponent, 
    CreatePostComponent,
    MapComponent,
    HomePageComponent,
    UpdatePostComponent,
    MapPostsComponent,
    TrendsComponent,
    AnalyticsComponent,
    UserProfileComponent,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    FormsModule,
    MatIconModule,
    MatCardModule,    
    ReactiveFormsModule,
    RouterModule,
    CommonModule
  ],
  providers: [
    provideAnimationsAsync(),
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }
  ],
  bootstrap: [AppComponent] 
})
export class AppModule { }
