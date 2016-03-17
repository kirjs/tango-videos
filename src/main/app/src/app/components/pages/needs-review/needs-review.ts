import {Component} from 'angular2/core';
import {Videos} from "../../entities/video/videos/videos";
import {VideoService} from "../../../services/VideoService";

export interface ReviewFilters {
    dancers: boolean,
    song: boolean,
    orquestra: boolean,
    genre: boolean,
    year: boolean
}


interface Filter {
    name: string,
    key: string,
    defaultValue: boolean
}
@Component({
    selector: 'needs-review',
    template: require('./needs-review.html'),
    styles: [require('./needs-review.css'), require('../../common/buttons.css')],
    providers: [],
    directives: [Videos],
    pipes: []
})
export class NeedsReview {
    videos:Array<any> = [];


    filters:Array<Filter> = [
        {
            name: 'Dancers',
            key: 'dancers',
            defaultValue: false
        },
        {
            name: 'Song name',
            key: 'song',
            defaultValue: false
        },
        {
            name: 'Orquestra',
            key: 'orquestra',
            defaultValue: false
        },
        {
            name: 'Song genre',
            key: 'genre',
            defaultValue: true
        },
        {
            name: 'Year',
            key: 'year',
            defaultValue: false
        }
    ];

    filterValues:any;

    updateFilter(filter, value) {
        this.filterValues[filter] = value;
        this.fetch();
    }

    fetch() {
        this.videoService.needsReview(this.filterValues).subscribe((videos)=> {
            this.videos = videos.slice(0,50);
        });
    }

    constructor(private videoService:VideoService) {
        this.filterValues = this.filters.reduce((result, filter)=> {
            result[filter.key] = filter.defaultValue;
            return result;
        }, {});
        this.fetch()
    }
}
