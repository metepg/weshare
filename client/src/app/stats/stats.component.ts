import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { SidebarService } from '../../services/sidebar/sidebar.service';
import { Tab, TabList, Tabs } from 'primeng/tabs';

@Component({
  selector: 'app-stats',
  imports: [RouterOutlet, Tabs, TabList, Tab],
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.scss'
})
export class StatsComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly sidebarService = inject(SidebarService);

  activeTab = '0';

  ngOnInit() {
    void this.router.navigate(['stats/chart']);
  }

  onTabChange(value: string | number) {
    switch (value) {
      case '0':
        void this.router.navigate(['stats/chart']);
        break;
      case '1':
        void this.router.navigate(['stats/search']);
        this.sidebarService.toggleSidebar(true);
        break;
      case '2':
        void this.router.navigate(['stats/user']);
        break;
      default:
        void this.router.navigate(['stats/chart']);
    }
  }
}
