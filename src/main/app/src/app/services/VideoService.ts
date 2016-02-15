import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';
import {BackendService} from "./BackendService";


@Injectable()
export class VideoService {
    constructor(private backendService:BackendService) {
    }

    list(skip:number, limit:number) {
        return this.backendService.read("videos/list/" + skip + "/" + limit);
    }

    addDancer(id:String, dancer:String) {
        return this.backendService.write(`videos/${id}/dancers/add`, {name: dancer});
    }

    removeDancer(id:String, dancer:String):any {
        return this.backendService.write(`videos/${id}/dancers/remove`, {name: dancer});
    }

    updateSongInfo(id:any, index:Number, field:String, data:String) {
        return this.backendService.write(`videos/${id}/songs/update`, {index, field, data});
    }


    needsReview():any {
        return this.backendService.read("videos/needsreview");
    }

    exists(id:String) {
        return this.backendService.read('videos/exist/' + id).map((result)=> {
            return !!result.length;
        });
    }

    add(id:String) {
        return this.backendService.write('videos/add', {id: id});
    }

    hide(id:string, value:boolean) {
        return this.backendService.write(`videos/${id}/hide`, {value: value});
    }

    update(id:string, field:String, value:String) {
        return this.backendService.write(`videos/${id}/update`, {field: field, value: value});
    }

    markComplete(id:String, value:boolean) {
        return this.backendService.write(`videos/${id}/markComplete`, {value: value});
    }
}