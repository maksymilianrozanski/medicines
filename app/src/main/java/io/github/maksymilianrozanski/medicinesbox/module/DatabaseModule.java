package io.github.maksymilianrozanski.medicinesbox.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.github.maksymilianrozanski.medicinesbox.annotation.DebugOpenClass;
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler;
@DebugOpenClass
@Module(includes = ContextModule.class)
public class DatabaseModule {

    @Provides
    public MedicinesDatabaseHandler medicinesDatabaseHandler(Context context){
        return new MedicinesDatabaseHandler(context);
    }
}
