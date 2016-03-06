import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable} from "rxjs";

@Injectable()
export class DancerService {
    constructor(private backendService:BackendService) {
    }

    get(id:String):Observable<any> {
        return this.backendService.read(`dancers/${id}`)
    }

    listNames():Observable<any> {
        return this.backendService.read('dancers/listNames');
    }

    list():Observable<any> {
        return this.backendService.read('dancers/list');
    }

    addDancer(id:any, name:any):any {
        return this.backendService.write('dancers/{id}/addPseudonym', {name})
    }
}
