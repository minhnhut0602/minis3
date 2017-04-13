import { Component, OnInit, Input } from '@angular/core';
import { NavService } from './nav.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  @Input() navItems;

  constructor(private navService: NavService) { }

  ngOnInit() {
    this.getShowNav();
  }

  getShowNav() {
    return this.navService.isVisible;
  }
}
