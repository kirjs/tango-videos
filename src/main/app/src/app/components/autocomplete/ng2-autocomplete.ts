import {Component, Directive, SkipSelf, Host, Optional, EventEmitter, Output, ElementRef, Input, Attribute} from 'angular2/core';
import { Observable } from 'rxjs/Rx';

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
    @Input() source:Observable<Array<String>> = new Observable();
    items:Array<String> = [];
    selectedIndex:number = 0;
    private dropdownOpen:boolean = false;
    sourceData:Array<String> = [];
    input:NgAutocompleteInput;
    value:string = '';

    openDropdown() {
        this.dropdownOpen = true;
    }

    closeDropdown() {
        this.dropdownOpen = false;
    }

    selectItem(index:number){
        this.closeDropdown();
        this.input.setValue(this.items[index]);
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
                if(this.dropdownOpen){
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
            return item.toLowerCase().indexOf(search) > -1;
        });

        this.updateSelectedIndex(this.items.reduce((index, item, itemIndex)=> {
            return (selectedItem === item) ? itemIndex : index;
        }, this.selectedIndex));
    }

    constructor() {

    }

    ngOnInit() {
        this.source.subscribe((data)=>{
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

