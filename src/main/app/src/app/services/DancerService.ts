import {Injectable} from '@angular/core';
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

    addPseudonym(id:string, name:string):any {
        return this.backendService.write(`dancers/${id}/addPseudonym`, {name})
    }

    removePseudonym(id:string, name:string):any {
        return this.backendService.write(`dancers/${id}/removePseudonym`, {name})
    }
}
