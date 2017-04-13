import { Injectable } from '@angular/core';

@Injectable()
export class NavService {
  public isVisible = true;
  constructor() { }

  toggleVisibility(): void {
    this.isVisible =  !this.isVisible;
  }

}
