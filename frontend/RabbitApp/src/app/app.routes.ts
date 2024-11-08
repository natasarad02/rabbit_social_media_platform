import { RouterModule, Routes } from '@angular/router';
import { ViewProfileComponent } from './pages/view-profile/view-profile.component';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';

export const routes: Routes = [
    { path: 'view-profile', component: ViewProfileComponent },
    { path: 'create-post', component: CreatePostComponent}
];
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }