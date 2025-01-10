import { Component, OnInit } from '@angular/core';
import { AnalyticsDTO } from '../../models/AnalyticsDTO.model';
import { AnalyticsService } from '../../services/analytics.service';
import { ChartData, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css'
})
export class AnalyticsComponent implements OnInit {
  analytics: AnalyticsDTO | null = null;

  // Pie chart data will be updated dynamically
  public pieChartData: ChartData<'pie'> = {
    labels: ['Post Activity', 'Comment Only Activity', 'No Activity'],
    datasets: [
      {
        data: [0, 0, 0],  
        backgroundColor: ['#D691D6', '#6C5B7B', '#9794C9'],  // Updated second color to match better
        hoverBackgroundColor: ['#C97BB5', '#5a4a62', '#7f7ab3'],  // Slightly darker shades for hover effect

 
      }
    ]
  };

  // Pie chart options
  public pieChartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top'
      },
      tooltip: {
        callbacks: {
          label: (tooltipItem) => {
            return tooltipItem.raw + '%';  // Show percentage in the tooltip
          }
        }
      }
    }
  };

  

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.getAnalyticsData().subscribe((data) => {
      this.analytics = data;
      console.log(this.analytics);

      // Update pie chart data with the fetched analytics data
      if (this.analytics) {
        this.pieChartData.datasets[0].data = [
          this.analytics.postPercentage,
          this.analytics.commentOnlyPercentage,
          this.analytics.noActivityPercentage
        ];
      }
    });
  }
}
