import {Component} from 'angular2/core';
import {BackendService} from "../../services/BackendService";
import {Observable} from 'rxjs';

@Component({
    selector: 'request-status',
    template: require('./request-status.html'),
    styles: [require('./request-status.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class RequestStatus {

    requests:Array<any> = [];

    constructor(backendService:BackendService) {
        backendService.progress.subscribe((observable:Observable) => {
            var request = {
                message: '?',
                actions: []
            };

            observable.subscribe(
                (data) => {
                    request.message = data.message;
                    request.actions = data.actions || [];
                },
                (error) => {

                },
                () => {
                    this.requests.splice(this.requests.indexOf(request), 1)
                }
            );

            this.requests.push(request);
        })
    }

}
