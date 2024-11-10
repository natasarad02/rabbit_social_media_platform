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
  filteredProfiles: ProfileViewDTO[] = [];
  paginatedProfiles: ProfileViewDTO[] = [];
  currentPage: number = 0; 
  itemsPerPage: number = 5; 
  totalItems: number = 0; 
  totalPages: number = 0;
  profileIds: number[] = [];

  searchCriteria = {
    name: '',
    surname: '',
    email: '',
    minPosts: null as number | null,
    maxPosts: null as number | null
  };

  sortCriteria: string = ''; 
  sortDirection: 'asc' | 'desc' = 'asc';

  sortEmail: string = '';
  sortFolowing: string = '';

  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.loadProfiles(); 
  }

  private loadProfiles(): void {
    this.profileService.getAllProfiles().subscribe(
      (response) => {
        console.log(response)
        console.log("ovde")
        this.profiles = response;
        this.filteredProfiles = this.profiles; 
        this.updateProfileIds();
        console.log(response);
        
      },
      (error) => {
        console.error('Error loading profiles', error);
      }
    );
  }

  

  private loadProfilesPaged(): void {
    this.profileService.getPaginatedProfiles(this.currentPage, this.itemsPerPage, this.profileIds).subscribe(
      (response) => {
        console.log(response)
        console.log("ovde")
        this.paginatedProfiles = response.content;
        this.totalItems = response.totalElements; 
        this.totalPages = response.totalPages;  
        console.log(response);
      },
      (error) => {
        console.error('Error loading profiles', error);
      }
    );
  }

  filterProfiles(): void {
    this.filteredProfiles = this.profiles.filter(profile => {
      const matchesName = profile.name.toLowerCase().includes(this.searchCriteria.name.toLowerCase());
      const matchesSurname = profile.surname.toLowerCase().includes(this.searchCriteria.surname.toLowerCase());
      const matchesEmail = profile.email.toLowerCase().includes(this.searchCriteria.email.toLowerCase());
      const matchesMinPosts = this.searchCriteria.minPosts == null || profile.postCount >= this.searchCriteria.minPosts;
      const matchesMaxPosts = this.searchCriteria.maxPosts == null || profile.postCount <= this.searchCriteria.maxPosts;

      return matchesName && matchesSurname && matchesEmail && matchesMinPosts && matchesMaxPosts;
    });
    this.updateProfileIds();
    this.sortProfiles();
  }

  sortProfiles(): void {
    const direction = this.sortDirection === 'asc' ? 1 : -1;

    this.filteredProfiles.sort((a, b) => {
      if (this.sortCriteria === 'followingCount') {
        return (a.followingCount - b.followingCount) * direction;
      } else if (this.sortCriteria === 'email') {
        return a.email.localeCompare(b.email) * direction;
      }
      return 0;
    });

    this.updateProfileIds();
  }

  setSortCriteria(criteria: string): void {
    if (this.sortCriteria === criteria) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc'; 
    } else {
      this.sortCriteria = criteria;
      this.sortDirection = 'asc'; 
    }
    this.sortProfiles();
  }

  private updateProfileIds(): void {
    this.profileIds = this.filteredProfiles.map(profile => profile.id);
    this.loadProfilesPaged();
  }

  changePage(page: number): void {
    if (page < 0 || page >= this.totalPages) return; 
    this.currentPage = page;
    this.loadProfilesPaged(); 
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadProfilesPaged();
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadProfilesPaged();
    }
  }

  
}



