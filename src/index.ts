import {
  NativeModulesProxy,
  EventEmitter,
  Subscription,
} from "expo-modules-core";

// Import the native module. On web, it will be resolved to ExpoUrovoScannerModule.web.ts
// and on native platforms to ExpoUrovoScannerModule.ts
import ExpoUrovoScannerModule from "./ExpoUrovoScannerModule";
import {
  ChangeEventPayload,
  ExpoUrovoScannerModuleViewProps,
} from "./ExpoUrovoScannerModule.types";

// Get the native constant value.
export const PI = ExpoUrovoScannerModule.PI;

export function hello(): string {
  return ExpoUrovoScannerModule.hello();
}

export function scan(): void {
  ExpoUrovoScannerModule.scan();
}

export function scanner(): void {
  return ExpoUrovoScannerModule.scanner();
}

export async function setValueAsync(value: string) {
  return await ExpoUrovoScannerModule.setValueAsync(value);
}

const emitter = new EventEmitter(
  ExpoUrovoScannerModule ?? NativeModulesProxy.ExpoUrovoScannerModule
);

export function addChangeListener(
  listener: (event: ChangeEventPayload) => void
): Subscription {
  return emitter.addListener<ChangeEventPayload>("onChange", listener);
}

export { ExpoUrovoScannerModuleViewProps, ChangeEventPayload };
