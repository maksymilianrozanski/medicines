package maksymilianrozanski.github.io.medicinesbox.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import maksymilianrozanski.github.io.medicinesbox.annotation.DebugOpenClass;
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler;
@DebugOpenClass
@Module(includes = ContextModule.class)
public class DatabaseModule {

    @Provides
    public MedicinesDatabaseHandler medicinesDatabaseHandler(Context context){
        return new MedicinesDatabaseHandler(context);
    }
}
