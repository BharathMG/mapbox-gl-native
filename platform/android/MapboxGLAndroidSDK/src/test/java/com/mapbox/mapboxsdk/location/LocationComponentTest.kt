package com.mapbox.mapboxsdk.location

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Looper
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.mapboxsdk.R
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocationComponentTest {
  private lateinit var locationComponent: LocationComponent

  @Mock
  private lateinit var locationComponentOptions: LocationComponentOptions

  @Mock
  private lateinit var mapboxMap: MapboxMap

  @Mock
  private lateinit var context: Context

  @Mock
  private lateinit var locationEngine: LocationEngine

  @Mock
  private lateinit var locationEngineRequest: LocationEngineRequest

  @Mock
  private lateinit var currentListener: LocationComponent.CurrentLocationEngineCallback

  @Mock
  private lateinit var lastListener: LocationComponent.LastLocationEngineCallback

  @Mock
  private lateinit var compassEngine: CompassEngine

  @Mock
  private lateinit var locationLayerController: LocationLayerController

  @Mock
  private lateinit var locationCameraController: LocationCameraController

  @Mock
  private lateinit var locationAnimatorCoordinator: LocationAnimatorCoordinator

  @Mock
  private lateinit var staleStateManager: StaleStateManager

  @Mock
  private lateinit var locationEngineProvider: LocationComponent.InternalLocationEngineProvider

  @Mock
  private lateinit var style: Style

  @Before
  fun before() {
    MockitoAnnotations.initMocks(this)
    locationComponent = LocationComponent(mapboxMap, currentListener, lastListener, locationLayerController, locationCameraController, locationAnimatorCoordinator, staleStateManager, compassEngine, locationEngineProvider)
    doReturn(locationEngine).`when`(locationEngineProvider).getBestLocationEngine(context, false)
    doReturn(style).`when`(mapboxMap).style
  }

  @Test
  fun activateWithRequestTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)

    Assert.assertEquals(locationEngineRequest, locationComponent.locationEngineRequest)

    doReturn(mock(TypedArray::class.java)).`when`(context)
      .obtainStyledAttributes(R.style.mapbox_LocationComponent, R.styleable.mapbox_LocationComponent)

    val resources = mock(Resources::class.java)

    doReturn(resources).`when`(context).resources
    doReturn(0f).`when`(resources)
      .getDimension(R.dimen.mapbox_locationComponentTrackingMultiFingerMoveThreshold)
    doReturn(0f).`when`(resources)
      .getDimension(R.dimen.mapbox_locationComponentTrackingMultiFingerMoveThreshold)
    locationComponent.activateLocationComponent(context, mockk(), true, locationEngineRequest)
    Assert.assertEquals(locationEngineRequest, locationComponent.locationEngineRequest)
  }

  @Test
  fun locationUpdatesWhenEnabledDisableTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)
    verify(locationEngine, times(0)).removeLocationUpdates(currentListener)
    verify(locationEngine, times(0)).requestLocationUpdates(eq(locationEngineRequest), eq(currentListener), any(Looper::class.java))

    locationComponent.onStart()
    verify(locationEngine, times(0)).removeLocationUpdates(currentListener)
    verify(locationEngine, times(0)).requestLocationUpdates(eq(locationEngineRequest), eq(currentListener), any(Looper::class.java))

    locationComponent.isLocationComponentEnabled = true
    verify(locationEngine).requestLocationUpdates(eq(locationEngineRequest), eq(currentListener), any(Looper::class.java))

    locationComponent.isLocationComponentEnabled = false
    verify(locationEngine).requestLocationUpdates(eq(locationEngineRequest), eq(currentListener), any(Looper::class.java))
    verify(locationEngine).removeLocationUpdates(currentListener)
  }

  @Test
  fun locationUpdatesWhenStartedStoppedTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)
    locationComponent.onStart()
    locationComponent.isLocationComponentEnabled = true

    locationComponent.onStop()
    verify(locationEngine).removeLocationUpdates(currentListener)

    locationComponent.onStart()
    verify(locationEngine, times(2)).requestLocationUpdates(eq(locationEngineRequest), eq(currentListener), any(Looper::class.java))
  }

  @Test
  fun locationUpdatesWhenNewRequestTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)
    locationComponent.onStart()
    locationComponent.isLocationComponentEnabled = true

    val newRequest = mock(LocationEngineRequest::class.java)
    locationComponent.locationEngineRequest = newRequest
    verify(locationEngine).removeLocationUpdates(currentListener)
    verify(locationEngine).requestLocationUpdates(eq(newRequest), eq(currentListener), any(Looper::class.java))
  }

  @Test
  fun lastLocationUpdateOnStartTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)
    locationComponent.onStart()
    locationComponent.isLocationComponentEnabled = true

    verify(locationEngine).getLastLocation(lastListener)
  }

  @Test
  fun transitionCallbackFinishedTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)
    locationComponent.onStart()
    locationComponent.isLocationComponentEnabled = true
    `when`(mapboxMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)

    val listener = mock(OnLocationCameraTransitionListener::class.java)

    val callback = ArgumentCaptor.forClass(OnLocationCameraTransitionListener::class.java)
    locationComponent.setCameraMode(CameraMode.TRACKING, listener)
    verify(locationCameraController).setCameraMode(eq(CameraMode.TRACKING), any(), callback.capture())
    callback.value.onLocationCameraTransitionFinished(CameraMode.TRACKING)

    verify(listener).onLocationCameraTransitionFinished(CameraMode.TRACKING)
    verify(locationAnimatorCoordinator).resetAllCameraAnimations(CameraPosition.DEFAULT, false)
  }

  @Test
  fun transitionCallbackCanceledTest() {
    locationComponent.activateLocationComponent(context, mockk(), locationEngine, locationEngineRequest, locationComponentOptions)
    locationComponent.onStart()
    locationComponent.isLocationComponentEnabled = true
    `when`(mapboxMap.cameraPosition).thenReturn(CameraPosition.DEFAULT)

    val listener = mock(OnLocationCameraTransitionListener::class.java)

    val callback = ArgumentCaptor.forClass(OnLocationCameraTransitionListener::class.java)
    locationComponent.setCameraMode(CameraMode.TRACKING, listener)
    verify(locationCameraController).setCameraMode(eq(CameraMode.TRACKING), any(), callback.capture())
    callback.value.onLocationCameraTransitionCanceled(CameraMode.TRACKING)

    verify(listener).onLocationCameraTransitionCanceled(CameraMode.TRACKING)
    verify(locationAnimatorCoordinator).resetAllCameraAnimations(CameraPosition.DEFAULT, false)
  }
}