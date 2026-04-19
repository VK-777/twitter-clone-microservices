import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { ProfileSetupRoutingModule } from './profile-setup-routing.module';
import { ProfileSetupComponent } from './profile-setup.component';

@NgModule({
  declarations: [ProfileSetupComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ProfileSetupRoutingModule
  ]
})
export class ProfileSetupModule { }
