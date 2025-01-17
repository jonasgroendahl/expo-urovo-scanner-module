# expo-urovo-scanner-module

A Expo Module for Android that communicates with Urovo scanners.

## Add the package to your npm dependencies

```
npm install expo-urovo-scanner-module
```

## How to use

This package exposes 2 methods `scanner` for initializing the scanner and `addChangeListener` for attaching a listener for scan events, which then can be acted upon.

Example usage:

```
import {
  addChangeListener,
  ChangeEventPayload,
  scanner,
} from "expo-urovo-scanner-module";

const [isScannerReady, setIsScannerReady] = useState(false);

const handleScanEvent = useCallback(
    (val: ChangeEventPayload) => {

    // do something
}, [])


useEffect(() => {
    console.log("[Scanner] Setting up listener, ready:", isScannerReady);

    let sub: Subscription | undefined;

    if (isScannerReady) {
      if (__DEV__) {
        console.log("[Scanner] Skipping listener in dev mode");
      } else {
        sub = addChangeListener(handleScanEvent);
        console.log("[Scanner] Listener added");
      }
    }

    return () => {
      if (sub) {
        console.log("[Scanner] Cleaning up listener");
        sub.remove();
      }
    };
  }, [isScannerReady, handleScanEvent]);


useEffect(() => {
    if (!__DEV__) {
        scanner();
    }
    setIsScannerReady(true);
}, []);
```

## Links

- Developer docs: https://en.urovo.com/developer/index.html
- Example projects with Java package: https://github.com/urovosamples/SDK_ReleaseforAndroid/tree/master/Samples/ScanManager (All example usage was written in Java, the logic for this module has been written in Kotlin and exposed as an Expo module.)
