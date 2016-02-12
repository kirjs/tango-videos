import {Component} from 'angular2/core';
import {DancerService} from "../../services/DancerService";

@Component({
  selector: 'dancers',
  template: require('./dancers.html'),
  styles: [require('./dancers.css')],
  providers: [],
  directives: [],
  pipes: []
})
export class Dancers {
  dancers:Array = [];
  fetch(){
    this.dancerService.list().subscribe((dancers)=> {
      this.dancers = dancers;
    });
  }
  constructor(private dancerService:DancerService) {
    this.fetch();
  }
}
