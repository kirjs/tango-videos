import {Component, Directive, SkipSelf, Host, Optional, EventEmitter, Output, ElementRef, Input, Attribute} from 'angular2/core';
import { Observable } from 'rxjs';
var removeDiacritics = require('diacritics').remove;


interface AutocompleteItem {
    key: String;
    value: String;
}


enum Action {UP, DOWN, ESC, CHOOSE}

var keyToActionMapping = {
    38: Action.UP,
    40: Action.DOWN,
    27: Action.ESC,
    13: Action.CHOOSE,
};

@Component({
    selector: 'ng2-autocomplete',
    template: require('./ng2-autocomplete.html'),
    styles: [require('./ng2-autocomplete.css')],

})
export class NgAutocompleteContainer {
    @Input() source:Observable<Array<String>> = Observable.from([[]]);
    items:Array<AutocompleteItem> = [];
    selectedIndex:number = 0;
    private dropdownOpen:boolean = false;
    sourceData:Array<AutocompleteItem> = [];
    input:NgAutocompleteInput;
    value:string = '';

    openDropdown() {
        this.dropdownOpen = true;
    }

    closeDropdown() {
        this.dropdownOpen = false;
    }

    selectItem(index:number) {
        this.closeDropdown();
        this.input.setValue(this.items[index].value);
    }

    handleKeydown(action:Action) {
        switch (action) {
            case Action.UP:
                this.openDropdown();
                this.updateSelectedIndex(this.selectedIndex - 1);
                break;
            case Action.DOWN:
                this.openDropdown();
                this.updateSelectedIndex(this.selectedIndex + 1);
                break;
            case Action.CHOOSE:
                if (this.dropdownOpen && this.items.length > 0) {
                    this.selectItem(this.selectedIndex);
                }
                break;
            case Action.ESC:
                this.selectedIndex = 0;
                this.closeDropdown();
                break;
        }
    };

    updateSelectedIndex(selectedIndex:number) {
        selectedIndex = Math.max(selectedIndex, 0);
        selectedIndex = Math.min(selectedIndex, this.items.length - 1);
        this.selectedIndex = selectedIndex;
    }


    filterValues(search:string) {
        let selectedItem = this.items[this.selectedIndex];
        search = search.toLowerCase();
        this.value = search;

        this.items = this.sourceData.filter(function (item) {
            return item.key.toLowerCase().indexOf(search) > -1;
        });

        this.updateSelectedIndex(this.items.reduce((index, item, itemIndex)=> {
            return (selectedItem === item) ? itemIndex : index;
        }, this.selectedIndex));
    }

    constructor() {

    }

    ngOnInit() {
        this.source.map((items) => {
            return items.map((item:any) => {
                if (typeof item === 'string') {
                    return {
                        key: removeDiacritics(item),
                        value: item
                    }
                }
                if (('value' in item) && !('key' in item)) {
                    item.key = removeDiacritics(item.value);
                }
                return item;
            });
        }).subscribe((data)=> {
            this.sourceData = data;
            this.filterValues(this.value);
        });
    }

    addInput(input:NgAutocompleteInput) {
        this.input = input;
        this.input.onUpdate
            .do(this.openDropdown.bind(this))
            .subscribe(this.filterValues.bind(this));

        this.input.onBlur.subscribe(this.closeDropdown.bind(this));

        this.input.onKeydown
            .map(event => event.keyCode)
            .filter(keyToActionMapping.hasOwnProperty.bind(keyToActionMapping))
            .map(keyCode => keyToActionMapping[keyCode])
            .subscribe(this.handleKeydown.bind(this));
    }
}

@Directive({
    // For some reason matches all inputs
    selector: 'ng2-autocomplete input',
    host: {
        '(input)': 'updateValue($event)'
    },
    providers: []
})
export class NgAutocompleteInput {
    value:String;
    onUpdate:EventEmitter<any>;
    onKeydown:Observable<any>;
    onBlur:Observable<any>;
    private nativeElement:any;

    setValue(value:String) {
        this.nativeElement.value = value;
    }

    updateValue(event) {
        this.value = event.target.value;
        this.onUpdate.emit(this.value);
    }

    constructor(@Optional() @SkipSelf() @Host() container:NgAutocompleteContainer, el:ElementRef) {
        if (container) {
            this.onUpdate = new EventEmitter();
            this.onKeydown = Observable.fromEvent(el.nativeElement, 'keydown');
            this.onBlur = Observable.fromEvent(el.nativeElement, 'blur');
            this.nativeElement = el.nativeElement;
            container.addInput(this);
        }
    }
}

