import {Component, Input} from 'angular2/core';
import {Http} from 'angular2/http';
import {Youtube} from '../../services/youtube';

class VideoInfo {
    constructor(private url:string,
                private title:string,
                private thumb:string) {
    }
}


@Component({
    selector: 'video-preview',
    templateUrl: 'app/components/videoPreview/videoPreview.html',
    styleUrls: ['app/components/videoPreview/videoPreview.css'],
    providers: [Youtube],
    directives: [],
})
export class VideoPreview {
    @Input() src:string;
    videoInfo:VideoInfo;


    constructor(private youtube:Youtube) {
    }

    ngOnInit() {
        let id = this.src.split('?v=')[1];
        this.youtube.getVideo(id).subscribe(data => {
            //noinspection TypeScriptUnresolvedVariable
            var snippet = data.items[0].snippet;

            this.videoInfo = new VideoInfo(id,
                snippet.title,
                snippet.thumbnails.default.url
            )
        });
    }

}
