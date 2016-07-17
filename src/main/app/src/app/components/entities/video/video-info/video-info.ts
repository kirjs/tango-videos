import {Component, Input} from '@angular/core';
import {DancerTile} from "../../dancer/dancer-tile/dancer-tile";
import {SongList} from "../../song/song-list/song-list";
import {EditableField} from "../../../common/editable-field/editable-field";
import {Icon} from "../../../common/icon/icon";
import {NeedsPermission} from "../../../common/needsPermission/needsPermission";
import {VideoService} from "../../../../services/VideoService";
import {ClickableSuggestion} from "../../../common/clickable-suggestion/clickable-suggestion";
import {SongService} from "../../../../services/SongService";
import {KeyValueUtils} from "../../../../interfaces/keyValue";


@Component({
    selector: 'video-info',
    template: require('./video-info.html'),
    styles: [require('./video-info.css')],
    providers: [],
    directives: [DancerTile, SongList, EditableField, Icon, NeedsPermission, ClickableSuggestion],
    pipes: []
})
export class VideoInfo {
    @Input() video:any;
    @Input() readonly:boolean = true;
    private orquestras = [];
    private songNames = [];
    private existingOrquestras:Array<String> = [];
    private existingSongNames:Array<String> = [];

    // TODO: We have 2 different date formats here, need to standardize
    // TODO: Ugly hack
    toDate(publishedAt:string, recordedAt:number) {
        if (recordedAt) {
            return (recordedAt == +recordedAt ) ? new Date(recordedAt * 1000) : new Date(recordedAt);
        } else {
            return new Date(publishedAt);
        }
    }

    handleUpdate(field, value) {
        if (field == 'recordedAt') {
            value = Date.parse(value) / 1000;
        }

        this.videoService.update(this.video.id, field, value).subscribe(x => {
            this.video[field] = value;
        });
    }

    addDancer(name:string) {
        this.videoService.addDancer(this.video.id, name).subscribe((data)=> {
            this.video.dancers = data;
        });
    }

    ngOnChanges() {
        this.updateExistingOrquestras(this.video.songs);
    }

    updateExistingOrquestras(songs) {
        this.existingOrquestras = songs.map(song=>song.orquestra);
        this.existingSongNames = songs.map(song=>song.name);
    }

    updateSong(info) {
        this.videoService.updateSongInfo(this.video.id, info.index, info.field, info.data).subscribe((song)=> {
            this.video.songs[info.index] = song;
            this.updateExistingOrquestras(this.video.songs);
        });
    }

    recover() {
        this.videoService.hide(this.video.id, false).subscribe(() => {
            this.video.shown = false;
        })
    }

    hide() {
        this.videoService.hide(this.video.id, true).subscribe(() => {
            this.video.shown = true;
        })
    }

    removeDancer(name:string) {
        this.videoService.removeDancer(this.video.id, name).subscribe((data)=> {
            this.video.dancers = data;
        });
    }

    static extractLastName(name:string) {
        var mappings = {
            'La Juan D\'arienzo': 'La Juan D\'arienzo',
            'Sexteto Tango': 'Sexteto Tango',
            'Uni Tango': 'Uni Tango',
            'Pasional orquesta': 'Pasional orquesta',
            'Solo tango orquestra': 'Solo tango',
            'Julio De Caro': 'De Caro',
            'Color Tango': 'Color Tango',
            'Escuela De Tango': 'Escuela De Tango',
            'Domingo Federico': 'Domingo Federico',
            'Sexteto Milonguero': 'Sexteto Milonguero',
            'Pablo Valle sextet': 'Pablo Valle sextet',
            'Fabio Hager Sexteto': 'Fabio Hager Sexteto',
            'Los reyes del tango': 'Los reyes del tango'

        };
        if (name in mappings) {
            return mappings[name];
        }
        return name.split(' ').pop();
    }


    updateEventName(eventName:String) {
        this.updateEvent(eventName, this.video.event.instance || this.toDate(this.video.publishedAt, this.video.recordedAt));
    }

    updateEventInstance(eventInstance:String) {
        this.updateEvent(this.video.event.name || '', eventInstance);
    }

    updateEvent(eventName:String, eventInstance:String) {
        this.videoService.updateEvent(this.video.id, eventName, eventInstance).subscribe(()=> {
            this.video.event.name = eventName;
            this.video.event.instance = eventInstance;
        });
    }

    constructor(private videoService:VideoService, private songService:SongService) {
        songService.listOrquestras().subscribe((orquestras)=> {
            this.orquestras =
                orquestras.map(r => KeyValueUtils.toKeyValue(VideoInfo.extractLastName(r.value), r.value));
        });
        songService.listNames().subscribe((names)=> {
            this.songNames = names.map(KeyValueUtils.valueObjectToKeyValue);
        });
    }
}
