import { Component, OnInit } from '@angular/core';
import { MetaData } from 'app/user/shared';
import { FileListService } from './file-list.service';

@Component({
  selector: 'app-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
export class FileListComponent implements OnInit {

  metaDatas: MetaData[] = [];

  constructor(
    private fileListService: FileListService
  ) { }

  ngOnInit() {
    this.fileListService.getAll().subscribe(data => this.metaDatas = data);
  }

}
