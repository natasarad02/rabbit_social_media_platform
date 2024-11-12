import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapPostsComponent } from './map-posts.component';

describe('MapPostsComponent', () => {
  let component: MapPostsComponent;
  let fixture: ComponentFixture<MapPostsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapPostsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapPostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
