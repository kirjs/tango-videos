import {Component} from 'angular2/core';
import {RouteParams} from 'angular2/router';
import {Observable} from "rxjs";
import {Videos} from "../../video/videos/videos";
import {DancerService} from "../../../../services/DancerService";
import {AddableField} from "../../../common/addable-field/addable-field";
import {EditableList} from "../../../common/editable-list/editable-list";

@Component({
    selector: 'dancer',
    template: require('./dancer.html'),
    styles: [require('./dancer.css')],
    providers: [],
    directives: [Videos, EditableList],
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

    removePseudonym(name){
        this.dancerService.removePseudonym(this.dancer.name, name).subscribe((pseudonyms)=> {
            this.dancer.pseudonyms = pseudonyms;
        });
    }

    constructor(private dancerService:DancerService, params:RouteParams) {
        this.dancerService.get(params.get('id')).subscribe((data) => {
            this.dancer = data;
        });
    }
}
