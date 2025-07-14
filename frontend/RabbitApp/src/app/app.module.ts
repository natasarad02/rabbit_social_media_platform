import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // <-- ADD THIS
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Angular Material Modules
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';

// ng2-charts
import { provideCharts, withDefaultRegisterables, BaseChartDirective } from 'ng2-charts';

// App-specific imports
import { AppRoutingModule } from './app.routes';
import { AppComponent } from './app.component';
import { TokenInterceptor } from './interceptor/TokenInterceptor';

// Component Imports
import { ViewProfileComponent } from './pages/view-profile/view-profile.component';
import { ViewPostsRegisteredComponent } from './pages/view-posts-registered/view-posts-registered.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { MapComponent } from './pages/map/map.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { UpdatePostComponent } from './pages/update-post/update-post.component';
import { MapPostsComponent } from './pages/map-posts/map-posts.component';
import { TrendsComponent } from './pages/trends/trends.component';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { ProfileComponent } from './pages/user-profile/user-profile.component';
import { PostComponent } from './pages/post/post.component';
import { ProfilePostsComponent } from './pages/user-profile/profile-posts/profile-posts.component';
import { ProfileInfoComponent } from './pages/user-profile/profile-info/profile-info.component';
import { ChatComponent } from './pages/chat/chat.component';
import { LoginComponent } from './pages/login/login.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';


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
    ProfileComponent,
    ProfileInfoComponent,
    ProfilePostsComponent,
    PostComponent,
    ChatComponent,
  ],
  imports: [
    // Angular Core Modules
    BrowserModule,
    BrowserAnimationsModule, // <-- ADD THIS
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,

    // Angular Material Modules
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,    
    MatListModule,
    MatSelectModule,
    MatRadioModule,

    // Other third-party modules
    BaseChartDirective,
    LoginComponent,
  ],
  providers: [
    provideAnimationsAsync(),
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    provideCharts(withDefaultRegisterables())
  ],
  bootstrap: [AppComponent] 
})
export class AppModule { }