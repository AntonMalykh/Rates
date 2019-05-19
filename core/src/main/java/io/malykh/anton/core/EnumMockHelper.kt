package io.malykh.anton.core

/**
 * Helper for mocking enums in function arguments using usual [Mockito] library.
 *
 * The pattern is the next:
 *      for (enumElement in MyEnum.values()){
 *           val mock = mock(EnumMockHelper::class.java) as EnumMockHelper<MyEnum>
 *           `when`(mock.get()).thenReturn(enumElement)
 *           `when`(someObject.funWithEnumArg(mock.get())).thenReturn(resultObject)
 *      }
 * Enums of the core library implement this.
 */
public interface EnumMockHelper<T> {
    fun get(): T
}