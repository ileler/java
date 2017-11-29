import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router'

import { InputTextModule, ButtonModule, DataTableModule, DialogModule, SelectButtonModule, DropdownModule, SharedModule as PrimeNGSharedModule } from 'primeng/primeng';

import { ValidateOnBlurDirective } from './directives';

@NgModule({
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, InputTextModule, ButtonModule, DataTableModule, DialogModule, SelectButtonModule, DropdownModule, PrimeNGSharedModule
  ],
  declarations: [
    ValidateOnBlurDirective
  ],
  providers: [
  ],
  exports: [
    CommonModule, FormsModule, ReactiveFormsModule, HttpModule, RouterModule,
    InputTextModule, ButtonModule, DataTableModule, DialogModule, SelectButtonModule, DropdownModule, PrimeNGSharedModule,
	  ValidateOnBlurDirective
  ]
})
export class SharedModule {
}
