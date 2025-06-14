import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { SidebarService } from '../../services/sidebar/sidebar.service';
import { TabViewChangeEvent, TabViewModule } from 'primeng/tabview';

@Component({
  selector: 'app-stats',
  imports: [
    TabViewModule,
    RouterOutlet
  ],
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.scss'
})
export class StatsComponent implements OnInit {

  constructor(private readonly router: Router, private readonly sidebarService: SidebarService) {}

  ngOnInit() {
    void this.router.navigate(['stats/chart']);
  }

  onTabChange(event: TabViewChangeEvent) {
    switch (event.index) {
      case 0:
        void this.router.navigate(['stats/chart']);
        break;
      case 1:
        void this.router.navigate(['stats/search']);
        this.sidebarService.toggleSidebar(true);
        break;
      case 2:
        void this.router.navigate(['stats/user']);
        break;
      default:
        void this.router.navigate(['stats/chart']);
    }
  }

  checkIfSearch(event: MouseEvent) {
    const tabViewElement = (event.target as HTMLElement).closest('li');
    if (tabViewElement) {
      const index = Array.from(tabViewElement.parentNode!.children).indexOf(tabViewElement);
      if (index === 1) {
        this.sidebarService.toggleSidebar(true);
      }
    }
  }
}
