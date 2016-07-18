import {Directive, Attribute, HostBinding, Input} from '@angular/core';

@Directive({
    selector: '[youtubeThumb]'
})
export class YoutubeThumb {
    private _id:string;

    @HostBinding('src') get src() {
        return this._id ? `https://i.ytimg.com/vi/${this._id}/mqdefault.jpg` : '';
    }

    @Input() set youtubeThumb(id:string) {
        this._id = id;
    };
}
