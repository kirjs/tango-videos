import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable} from "rxjs";
import {ReviewFilters} from "../components/pages/needs-review/needs-review";
import {URLSearchParams} from "angular2/http";


@Injectable()
export class VideoService {
    constructor(private backendService:BackendService) {
    }

    list(skip:number, limit:number):Observable<any> {
        return this.backendService.read("videos/list/" + skip + "/" + limit);
    }

    addDancer(id:String, dancer:String):Observable<any> {
        return this.backendService.write(`videos/${id}/dancers/add`, {name: dancer});
    }

    removeDancer(id:String, dancer:String):Observable<any> {
        return this.backendService.write(`videos/${id}/dancers/remove`, {name: dancer});
    }

    updateSongInfo(id:any, index:Number, field:String, data:String):Observable<any> {
        return this.backendService.write(`videos/${id}/songs/update`, {index, field, data});
    }

    needsReview(filters:any):Observable<any> {

        //noinspection TypeScriptUnresolvedFunction
        var urlSearchParams = Object.keys(filters).reduce((params, filter)=>{
            params.set(filter, filters[filter]);
            return params;
        }, new URLSearchParams());


        return this.backendService.read("videos/needsreview", urlSearchParams);
    }

    exists(id:String):Observable<any> {
        return this.backendService.read('videos/exist/' + id).map((result)=> {
            return !!result.length;
        });
    }

    add(id:String):Observable<any> {
        return this.backendService.write('videos/add', {id: id});
    }

    hide(id:string, value:boolean):Observable<any> {
        return this.backendService.write(`videos/${id}/hide`, {value: value});
    }

    update(id:string, field:String, value:String):Observable<any> {
        return this.backendService.write(`videos/${id}/update`, {field: field, value: value});
    }

    markComplete(id:String, value:boolean):Observable<any> {
        return this.backendService.write(`videos/${id}/markComplete`, {value: value});
    }
}
