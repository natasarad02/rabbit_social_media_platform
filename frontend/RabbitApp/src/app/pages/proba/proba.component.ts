import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-proba',
  standalone: true,
  imports: [
    CommonModule,
  ],
  template: `<p>proba works!</p>`,
  styleUrl: './proba.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProbaComponent { }
