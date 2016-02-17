import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable} from "rxjs";


@Injectable()
export class AdminToolsService {
    constructor(private backendService:BackendService) {
    }

    renameDancer(oldName:String, newName:String):Observable<any> {
        return this.backendService.write('adminTools/renameDancer', {oldName, newName});
    }

    stats():any {
        return this.backendService.read('adminTools/stats');
    }
}
