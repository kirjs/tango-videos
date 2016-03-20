import {Component} from 'angular2/core';
import {RouteParams} from 'angular2/router';
import {Observable} from "rxjs";
import {Router} from 'angular2/router';
import {Videos} from "../../video/videos/videos";
import {DancerService} from "../../../../services/DancerService";
import {EditableList} from "../../../common/editable-list/editable-list";
import {EditableField} from "../../../common/editable-field/editable-field";

@Component({
    selector: 'dancer',
    template: require('./dancer.html'),
    styles: [require('./dancer.css')],
    providers: [],
    directives: [Videos, EditableList, EditableField],
    pipes: []
})
export class Dancer {
    id:string;
    dancer:any;

    addPseudonym(name){
        this.dancerService.addPseudonym(this.dancer.name, name).subscribe((pseudonyms)=> {
            this.dancer.pseudonyms = pseudonyms;
        });
    }

    rename(name){
        this.dancerService.addPseudonym(name, this.dancer.name).subscribe(()=> {
            this.router.navigate(['Dancer', {id: name}])
        });
    }

    removePseudonym(name){
        this.dancerService.removePseudonym(this.dancer.name, name).subscribe((pseudonyms)=> {
            this.dancer.pseudonyms = pseudonyms;
        });
    }

    constructor(private dancerService:DancerService, params:RouteParams, private router:Router) {
        this.dancerService.get(params.get('id')).subscribe((data) => {
            this.dancer = data;
            this.dancer.videos = this.dancer.videos.slice(0, 20);
        });
    }
}
