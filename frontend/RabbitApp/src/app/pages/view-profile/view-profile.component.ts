import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile-service.service';
import { ProfileViewDTO } from '../../models/ProfileViewDTO.model';

@Component({
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrl: './view-profile.component.css',
})
export class ViewProfileComponent implements OnInit{

  profiles: ProfileViewDTO[] = [];

  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.loadProfiles(); // Extracted to a separate method for better readability
  }

  private loadProfiles(): void {
    this.profileService.getAllProfiles().subscribe(
      (profiles: ProfileViewDTO[]) => {
        this.profiles = profiles;
        console.log(profiles);
      },
      (error) => {
        console.error('Error loading profiles', error); // Log the error if needed
      }
    );
  }
}
