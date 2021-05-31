package id.rllyhz.core.di

import android.app.Application
import androidx.paging.PagedList
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.rllyhz.core.api.ApiEndpoint
import id.rllyhz.core.api.ApiEndpoint.Companion.BASE_URL
import id.rllyhz.core.data.SunGlassesShowRepository
import id.rllyhz.core.data.local.LocalDataSource
import id.rllyhz.core.data.local.SunGlassesShowDB
import id.rllyhz.core.data.local.SunGlassesShowDao
import id.rllyhz.core.data.remote.RemoteDataSource
import id.rllyhz.core.domain.usecase.SunGlassesShowInteractor
import id.rllyhz.core.domain.usecase.SunGlassesShowUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiEndpoint =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiEndpoint::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ): SunGlassesShowDB =
        Room.databaseBuilder(
            app,
            SunGlassesShowDB::class.java,
            "sunglassesshow.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(
        db: SunGlassesShowDB
    ): SunGlassesShowDao =
        db.getDao()

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiClient: ApiEndpoint
    ): RemoteDataSource =
        RemoteDataSource(apiClient)

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): SunGlassesShowRepository =
        SunGlassesShowRepository(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun providePagedConfig(): PagedList.Config =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(5)
            .setPageSize(5)
            .build()

    @Provides
    @Singleton
    fun provideUseCase(
        repository: SunGlassesShowRepository,
        pagedListConfig: PagedList.Config
    ): SunGlassesShowUseCase =
        SunGlassesShowInteractor(repository, pagedListConfig)
}